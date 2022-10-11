import { Subscription } from 'rxjs';
import { PostService } from './../../core/shared/posts/post-blocks.service';
import { Component, OnInit } from '@angular/core';
import { Post } from 'src/app/core/shared/posts/post';
import { ActivatedRoute, Router } from '@angular/router';
import { MatDialog } from '@angular/material/dialog';
import { ConfirmDialogComponent } from 'src/app/map-creator/confirm-dialog/confirm-dialog.component';
import { VisualCanvasComponent } from 'src/app/core/shared/export-image/visual-canvas/visual-canvas.component';
import { CommentService } from 'src/app/core/shared/posts/comment.service';
import { Comment } from 'src/app/core/shared/posts/comment';
import { MatSnackBar } from '@angular/material/snack-bar';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

@Component({
  selector: 'post-map-details',
  templateUrl: './post-details.component.html',
  styleUrls: ['./post-details.component.scss']
})
export class PostDetailsComponent implements OnInit {

  public carregando: boolean = false;
  public result: boolean = false;
  public post: Post;
  public sub: Subscription;
  public mapSize: number = window.innerHeight / 1.8;
  public currentUserPost: boolean = false;

  public commentMaxLength: number = 200;
  public form: FormGroup;

  public likeClass: string[];
  public dislikeClass: string[];
  public lastLikeValue: boolean;
  public lastDislikeValue: boolean;

  constructor(
    private postService: PostService,
    private commentService: CommentService,
    private route: ActivatedRoute,
    private router: Router,
    private dialog: MatDialog,
    private snackBar: MatSnackBar,
    private fb: FormBuilder,

  ) { }

  ngOnInit(): void {

    this.sub = this.route.params.subscribe(
      params => {
        this.getPost(params['id']);
      });

    this.form = this.fb.group({
      content: ['', [Validators.required, Validators.maxLength(3), Validators.maxLength(this.commentMaxLength)]],
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
    this.result = false;
    this.carregando = true;
    this.postService.getPostById(id).subscribe(
      {
        next: result => {
          this.post = result.body;
          this.carregando = false;
          this.currentUserPost = true;
          this.result = true;
          this.viewPost();
        },
        error: error => {
          this.getPublicPost(id)
          this.carregando = false;
        }
      })
  }

  getPublicPost(id: number) {
    this.carregando = true;
    this.postService.getPublicPostsById(id).subscribe(
      {
        next: result => {
          this.post = result.body;
          this.carregando = false;
          this.currentUserPost = false;
          this.result = true;
          this.viewPost();
        },
        error: error => {
          console.log(error)
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

  getComments() {
    this.carregando = true;
    this.commentService.getPostComments(this.post.id).subscribe(
      {
        next: result => {
          this.post.comments = result.body;
          this.carregando = false;
        },
        error: error => {
          this.carregando = false;
        }
      })
  }

  commentPost() {
    this.carregando = true;
    let p = Object.assign({}, this.form.value);
    this.commentService.postComment(p, this.post.id).subscribe(
      {
        next: result => {
          this.snackBar.open("Comentário feito!", "Ok", {
            duration: 2000
          })
          this.form.patchValue({ content: '' });
          this.updateComments();
          this.carregando = false;
        },
        error: error => {
          this.carregando = false;
        }
      })
  }

  getChildComments(id: number) {
    var childComments = this.post.comments.filter(c => c.repliedTo?.id == id);
    return childComments.length > 0 ? childComments : null;
  }

  getPrimaryComments() {
    var primaryComments = this.post.comments.filter(c => c.repliedTo == null);
    return primaryComments.length > 0 ? primaryComments : null;
  }

  updateComments() {
    this.carregando = true;
    this.commentService.getPostComments(this.post.id).subscribe(
      {
        next: result => {
          this.post.comments = result.body;
          this.carregando = false;
        },
        error: error => {
          this.carregando = false;
        }
      })
  }

  likePost() {
    this.lastLikeValue = this.lastLikeValue == true ? false : true;

    this.postService.likePost(this.lastLikeValue, this.post.id).subscribe(
      {
        next: result => {
          this.likeClass = this.lastLikeValue ? ['rated'] : [];
          if (!this.lastDislikeValue) this.dislikeClass = [];
          if (this.lastDislikeValue && this.lastLikeValue) this.dislikePost();

          if (this.currentUserPost) this.getPost(this.post.id);
          else this.getPublicPost(this.post.id)
        },
        error: error => {
        }
      })
  }

  dislikePost() {
    this.lastDislikeValue = this.lastDislikeValue == true ? false : true;


    this.postService.dislikePost(this.lastDislikeValue, this.post.id).subscribe(
      {
        next: result => {
          this.dislikeClass = this.lastDislikeValue ? ['rated'] : [];
          if (!this.lastLikeValue) this.likeClass = [];
          if (this.lastLikeValue && this.lastDislikeValue) this.likePost();

          if (this.currentUserPost) this.getPost(this.post.id);
          else this.getPublicPost(this.post.id)
        },
        error: error => {
        }
      })
  }

  viewPost() {
    this.postService.viewPost(this.post.id).subscribe(
      {
        next: result => {

        },
        error: error => {
        }
      })
  }
}
