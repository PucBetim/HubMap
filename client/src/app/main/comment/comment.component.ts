import { Post } from 'src/app/core/shared/posts/post';
import { ConfigService } from 'src/app/core/services/config.service';
import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';

import { Comment } from 'src/app/core/shared/posts/comment';
import { CommentService } from 'src/app/core/shared/posts/comment.service';
import { Router } from '@angular/router';
import { MatDialog } from '@angular/material/dialog';
import { ConfirmDialogComponent } from 'src/app/map-creator/confirm-dialog/confirm-dialog.component';

@Component({
  selector: 'comment',
  templateUrl: './comment.component.html',
  styleUrls: ['./comment.component.scss']
})
export class CommentComponent implements OnInit {

  @Output() commentMadeEvent = new EventEmitter<any>();

  @Input() post: Post;
  @Input() comment: Comment;
  @Input() response: boolean = false;
  @Input() order: number;


  public loading: boolean = false;
  public commentMaxLength: number = 200;

  public lastLikeValue: boolean;
  public lastDislikeValue: boolean;
  public likeClass: string[];
  public dislikeClass: string[];

  public form: FormGroup;
  public formEdit: FormGroup;

  public responseOpened: boolean = false;
  public editOpened: boolean = false;
  public repliesOpenedState: boolean = false;

  public currentUserComment: boolean = false;

  constructor(
    private fb: FormBuilder,
    private commentService: CommentService,
    private snackBar: MatSnackBar,
    private router: Router,
    private dialog: MatDialog,
  ) { }

  ngOnInit(): void {
    if (ConfigService.getUser())
      this.currentUserComment = ConfigService.getUser().id == this.comment.author.id ? true : false;

    this.form = this.fb.group({
      content: ['', [Validators.required, Validators.maxLength(3), Validators.maxLength(this.commentMaxLength)]],
    });

    this.formEdit = this.fb.group({
      content: ['', [Validators.required, Validators.maxLength(3), Validators.maxLength(this.commentMaxLength)]],
    });
  }



  openReply() {
    if (!ConfigService.getUser()) {
      this.router.navigate(['session/create-account']);
      return;
    }
    this.responseOpened = true
  }

  replyComment() {
    if (!ConfigService.getUser()) {
      this.router.navigate(['session/create-account']);
      return;
    }

    this.loading = true;
    let p = Object.assign({}, new Comment, this.form.value);
    p.repliedTo = this.comment;
    this.commentService.postComment(p, this.post.id).subscribe(
      {
        next: result => {
          this.snackBar.open("Comentário feito!", "Ok", {
            duration: 2000
          })
          this.addReply(result.body.dataId);
          this.form.patchValue({ content: '' });
          this.responseOpened = false;
          this.loading = false;
        },
        error: error => {
          this.loading = false;
        }
      })
  }

  addReply(id: string) {
    this.commentService.getCommentById(id).subscribe(
      {
        next: result => {
          this.post.comments.push(result.body)
        },
        error: error => {
          this.loading = false;
          this.snackBar.open("Ocorreu um erro ao carregar o comentário!", "Ok");
        }
      })
  }


  getChildComments() {
    var childComments = this.post.comments.filter(c => c.repliedTo?.id == this.comment.id);
    return childComments.length > 0 ? childComments : null;
  }

  deleteComment() {
    this.post.comments = this.post.comments.filter(e => e.id != this.comment.id);
  }

  likeComment() {
    if (!ConfigService.getUser()) {
      this.router.navigate(['session/create-account']);
      return;
    }

    this.lastLikeValue = this.lastLikeValue == true ? false : true;

    this.commentService.likeComment(this.lastLikeValue, this.comment.id).subscribe(
      {
        next: result => {
          this.likeClass = this.lastLikeValue ? ['rated'] : [];
          if (!this.lastDislikeValue) this.dislikeClass = [];
          if (this.lastDislikeValue && this.lastLikeValue) this.dislikeComment()
          else this.refreshComment();
        },
        error: error => {
        }
      })
  }

  dislikeComment() {
    this.lastDislikeValue = this.lastDislikeValue == true ? false : true;


    this.commentService.dislikeComment(this.lastDislikeValue, this.comment.id).subscribe(
      {
        next: result => {
          this.dislikeClass = this.lastDislikeValue ? ['rated'] : [];
          if (!this.lastLikeValue) this.likeClass = [];
          if (this.lastLikeValue && this.lastDislikeValue) this.likeComment()
          else this.refreshComment();
        },
        error: error => {
        }
      })
  }

  refreshComment() {
    this.commentService.getCommentById(this.comment.id).subscribe(
      {
        next: result => {
          this.comment = result.body;
          this.getChildComments();
        },
        error: error => {
          this.snackBar.open("Ocorreu um erro ao carregar o comentário!", "Ok");
        }
      })
  }

  openDelete() {
    var confirmDeleteConfig = {
      disableClose: false,
      width: 'auto',
      data: {
        titulo: "Deletar Bloco",
        texto: "Tem certeza que deseja deletar o comentário? Essa ação não pode ser revertida.",
      }
    };

    const dialogRef = this.dialog.open(ConfirmDialogComponent, confirmDeleteConfig);
    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.loading = true;
        this.commentService.deleteComment(this.comment.id).subscribe({
          next: result => {
            this.deleteComment();
            this.snackBar.open("Comentário excluído com sucesso.", "Ok");
            this.loading = false;
          }, error: error => {
            this.snackBar.open("Falha ao excluir comentário! Tente novamente mais tarde.", "Ok");
            this.loading = false;
          }
        })
      }
    });
  }

  openEdit() {
    this.editOpened = true;
    this.formEdit.patchValue({ content: this.comment.content });
  }

  updateComment() {
    this.loading = true;
    let comment = Object.assign({}, this.comment);
    comment.content = this.formEdit.value.content;

    this.commentService.updateComment(comment, comment.id).subscribe({
      next: result => {
        this.editOpened = false;
        this.refreshComment();
        this.loading = false;
      }, error: error => {
        this.loading = false;
      }
    })
  }
}
