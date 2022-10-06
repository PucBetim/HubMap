import { Subscription } from 'rxjs';
import { PostService } from './../../core/shared/posts/post-blocks.service';
import { Component, OnInit } from '@angular/core';
import { Post } from 'src/app/core/shared/posts/post';
import { ActivatedRoute, Router } from '@angular/router';
import { MatDialog } from '@angular/material/dialog';
import { ConfirmDialogComponent } from 'src/app/map-creator/confirm-dialog/confirm-dialog.component';
import { VisualCanvasComponent } from 'src/app/core/shared/export-image/visual-canvas/visual-canvas.component';

@Component({
  selector: 'post-map-details',
  templateUrl: './post-details.component.html',
  styleUrls: ['./post-details.component.scss']
})
export class PostDetailsComponent implements OnInit {

  // @HostListener('window:resize', ['$event'])
  // onResize() {
  //   if (window.innerWidth > 1400 && this.mapSize != 500)
  //     this.resizeMapDisplay();
  //   else if (window.innerWidth > 1200 && window.innerWidth < 1401 && this.mapSize != 300)
  //     this.resizeMapDisplay();
  //   else if (window.innerWidth > 800 && window.innerWidth < 1201 && this.mapSize != 200)
  //     this.resizeMapDisplay();
  //   else if (window.innerWidth < 801 && this.mapSize != 100)
  //     this.resizeMapDisplay();
  // }

  public carregando: boolean = false;
  public result: boolean = false;
  public post: Post;
  public sub: Subscription;
  public mapSize: number = window.innerHeight / 1.8;


  constructor(
    private postService: PostService,
    private route: ActivatedRoute,
    private router: Router,
    private dialog: MatDialog,
  ) { }

  ngOnInit(): void {
    this.sub = this.route.params.subscribe(
      params => {
        this.getPost(params['id']);
      });

  }

  resizeMapDisplay() {
    this.mapSize = window.innerHeight / 1.5;
    this.result = false;
    setTimeout(() => {
      this.result = true;
    }, 200)
  }

  getPost(id: number) {
    this.carregando = true;
    this.postService.getPostById(id).subscribe(
      {
        next: result => {
          this.post = result.body;
          this.carregando = false;
          this.result = true;
        },
        error: error => {
          console.log(error);
          this.carregando = false;
        }
      })
  }


  async downloadImage() {
    var exportImageConfig = {
      disableClose: false,
      width: 'auto',
      height: 'auto',
      data: {
        blocks: this.post.map
      }
    };

    const dialogRef = this.dialog.open(VisualCanvasComponent, exportImageConfig);
    dialogRef.afterClosed().subscribe(result => {
      if (result) {
      }
    });
  }

  goToEdit() {
    this.router.navigateByUrl(`/creator/${this.post.id}`);
  }

  deletePostConfirm() {
    var confirmDeleteConfig = {
      disableClose: false,
      width: 'auto',
      data: {
        titulo: "Deletar Mapa",
        texto: "Tem certeza que deseja deletar o mapa selecionado? Essa ação não pode ser revertida.",
        danger: true,
      }
    };
    const dialogRef = this.dialog.open(ConfirmDialogComponent, confirmDeleteConfig);
    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.deletePost()
      }
    });
  }

  deletePost() {
    this.carregando = true;
    this.postService.deletePost(this.post.id).subscribe(
      {
        next: result => {
          this.router.navigate(['/session/settings'])
          this.carregando = false;
        },
        error: error => {
          this.carregando = false;
        }
      })
  }
}
