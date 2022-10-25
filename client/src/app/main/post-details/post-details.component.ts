import { ConfigService } from './../../core/services/config.service';
import { Subscription } from 'rxjs';
import { PostService } from './../../core/shared/posts/post-blocks.service';
import { Component, HostListener, OnInit } from '@angular/core';
import { Block, Post } from 'src/app/core/shared/posts/post';
import { ActivatedRoute, Router } from '@angular/router';
import { MatDialog } from '@angular/material/dialog';
import { ConfirmDialogComponent } from 'src/app/map-creator/confirm-dialog/confirm-dialog.component';
import { VisualCanvasComponent } from 'src/app/core/shared/export-image/visual-canvas/visual-canvas.component';
import { CommentService } from 'src/app/core/shared/posts/comment.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { User } from 'src/app/session/models/user';

@Component({
  selector: 'post-map-details',
  templateUrl: './post-details.component.html',
  styleUrls: ['./post-details.component.scss']
})
export class PostDetailsComponent implements OnInit {

  @HostListener('window:resize', ['$event'])
  onResize() {
    this.mapSize = 0;
    this.mapZoomSize = 0;
    setTimeout(() => {
      this.mapSize = window.innerHeight / 1.8;
      this.mapZoomSize = window.innerWidth / 1.6;
    }, 1000)
  }

  public loading: boolean = false;

  public post: Post;
  public sub: Subscription;
  public mapSize: number = window.innerHeight / 1.8;
  public mapZoomSize: number = window.innerWidth / 1.6;

  public currentUserPost: boolean = false;
  public loggedUser: User;

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
    this.loggedUser = ConfigService.getUser()

    this.sub = this.route.params.subscribe(
      params => {
        if (this.loggedUser)
          this.currentUserPost = this.loggedUser.id == params['authorId'] ? true : false;
        this.getPost(params['id']);
      });

    this.form = this.fb.group({
      content: ['', [Validators.required, Validators.maxLength(3), Validators.maxLength(this.commentMaxLength)]],
    });
  }

  getPost(id: string) {
    this.loading = true;
    if (this.currentUserPost) {
      this.postService.getPostById(id).subscribe(
        {
          next: result => {
            this.post = result.body;
            this.loading = false;
            this.viewPost();
            this.getComments();
          },
          error: error => {
            this.snackBar.open("Falha ao obter post! Tente novamente mais tarde.", "Ok");
            this.loading = false;
            return;
          }
        })
    }
    else {
      this.postService.getPublicPostsById(id).subscribe(
        {
          next: result => {
            this.post = result.body;
            this.loading = false;
            if (this.loggedUser)
              this.viewPost();
            this.getComments();
          },
          error: error => {
            this.snackBar.open("Falha ao obter post! Tente novamente mais tarde.", "Ok");
            this.loading = false;
            return;
          }
        })
    }
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
    this.loading = true;
    this.postService.deletePost(this.post.id).subscribe({
      next: result => {
        this.router.navigate(['/session/settings'])
        this.loading = false;
      },
      error: error => {
        this.loading = false;
      }
    })
  }

  getComments() {
    this.loading = true;
    if (this.currentUserPost) {
      this.commentService.getPostComments(this.post.id).subscribe(
        {
          next: result => {
            this.post.comments = result.body;
            this.loading = false;
          },
          error: error => {
            this.snackBar.open("Erro ao obter comentários! Tente novamente mais tarde.", "Ok");
            this.loading = false;
          }
        })
    }
    else {
      this.commentService.getPublicPostComments(this.post.id).subscribe(
        {
          next: result => {
            this.post.comments = result.body;
            this.loading = false;
          },
          error: error => {
            this.snackBar.open("Erro ao obter comentários! Tente novamente mais tarde.", "Ok");
            this.loading = false;
          }
        })
    }
  }

  commentPost() {
    if (!this.loggedUser) {
      this.router.navigate(['session/create-account', { savedRoute: this.router.url }]);
      return;
    }

    this.loading = true;
    let p = Object.assign({}, this.form.value);
    this.commentService.postComment(p, this.post.id).subscribe({
      next: result => {
        this.snackBar.open("Comentário feito!", "Ok", {
          duration: 2000
        })
        this.form.patchValue({ content: '' });
        this.getComments();
        this.loading = false;
      },
      error: error => {
        this.loading = false;
      }
    })
  }

  getChildComments(id: number) {
    if (this.post.comments[0]) {
      var childComments = this.post.comments.filter(c => c.repliedTo?.id == id);
      return childComments.length > 0 ? childComments : null;
    }
    return;
  }

  getPrimaryComments() {
    if (this.post.comments) {
      var primaryComments = this.post.comments.filter(c => c.repliedTo == null);
      return primaryComments.length > 0 ? primaryComments : null;
    }
    return null;
  }

  likePost() {
    if (!this.loggedUser) {
      this.router.navigate(['session/create-account', { savedRoute: this.router.url }]);
      return;
    }

    this.lastLikeValue = this.lastLikeValue == true ? false : true;
    this.postService.likePost(this.lastLikeValue, this.post.id).subscribe({
      next: result => {
        this.likeClass = this.lastLikeValue ? ['rated'] : [];
        if (!this.lastDislikeValue) this.dislikeClass = [];
        if (this.lastDislikeValue && this.lastLikeValue) this.dislikePost();
        if (this.currentUserPost) this.getPost(this.post.id);
        else this.getPost(this.post.id)
      },
      error: error => {
        this.snackBar.open("Erro ao avaliar comentário! Tente novamente mais tarde.")
      }
    })
  }

  dislikePost() {
    if (!ConfigService.getUser()) {
      this.router.navigate(['session/create-account', { savedRoute: this.router.url }]);
      return;
    }

    this.lastDislikeValue = this.lastDislikeValue == true ? false : true;
    this.postService.dislikePost(this.lastDislikeValue, this.post.id).subscribe(
      {
        next: result => {
          this.dislikeClass = this.lastDislikeValue ? ['rated'] : [];
          if (!this.lastLikeValue) this.likeClass = [];
          if (this.lastLikeValue && this.lastDislikeValue) this.likePost();
          if (this.currentUserPost) this.getPost(this.post.id);
          else this.getPost(this.post.id)
        },
        error: error => {
          this.snackBar.open("Erro ao avaliar comentário! Tente novamente mais tarde.")
        }
      })
  }

  // forkPost() {
  //   if (!this.currentUserPost) {
  //     this.loading = true;
  //     let p = new Post;
  //     p.title = this.post.title += ' (cópia)';
  //     p.description = this.post.description;
  //     p.private = true;
  //     this.postService.postPost(p).subscribe({
  //       next: obj => {
  //         this.forkBlocks(obj.body.dataId)
  //         this.loading = false;
  //       }, error: error => {
  //         this.loading = false;
  //       }
  //     })
  //   }
  // }
  // forkBlocks(id: string) {
  //   var blocks = this.removeId(this.post.map);
  //   blocks.forEach(b => {
  //     this.loading = true;
  //     this.postService.postBlocks(b, id).subscribe({
  //       next: obj => {
  //         this.loading = false;
  //       }, error: error => {
  //         this.loading = false;
  //       }
  //     })
  //   });
  // }

  // removeId(block: Block[]){
  //   block.forEach(b => {
  //     delete b.id;
  //     this.removeId(b.blocks);
  //     return;
  //   });
  //   return block;
  // }

  viewPost() {
    this.postService.viewPost(this.post.id).subscribe()
  }

  sharePost() {
    navigator.clipboard.writeText(window.location.href)
    this.snackBar.open("Link copiado para a área de transferência!", "Ok")

  }
}
