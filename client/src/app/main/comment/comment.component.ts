import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';

import { Comment } from 'src/app/core/shared/posts/comment';
import { CommentService } from 'src/app/core/shared/posts/comment.service';

@Component({
  selector: 'comment',
  templateUrl: './comment.component.html',
  styleUrls: ['./comment.component.scss']
})
export class CommentComponent implements OnInit {

  @Output() commentMadeEvent = new EventEmitter<any>();

  @Input() postComments: Comment[];
  @Input() comment: Comment;
  @Input() postId: number;
  @Input() childComments: Comment[];
  @Input() response: boolean = false;
  @Input() order: number;


  public carregando: boolean = false;
  public form: FormGroup;
  public commentMaxLength: number = 200;
  public openResponse: boolean = false;
  public repliesOpenedState: boolean = false;

  public lastLikeValue: boolean;
  public lastDislikeValue: boolean;
  public likeClass: string[];
  public dislikeClass: string[];

  constructor(
    private fb: FormBuilder,
    private commentService: CommentService,
    private snackBar: MatSnackBar,
  ) { }

  ngOnInit(): void {
    this.form = this.fb.group({
      content: ['', [Validators.required, Validators.maxLength(3), Validators.maxLength(this.commentMaxLength)]],
    });
  }

  replyComment() {

    this.carregando = true;
    let p = Object.assign({}, new Comment, this.form.value);
    p.repliedTo = this.comment;
    this.commentService.postComment(p, this.postId).subscribe(
      {
        next: result => {
          this.snackBar.open("Comentário feito!", "Ok", {
            duration: 2000
          })
          this.emitCommentMadeEvent();
          this.openResponse = false;
          this.carregando = false;
        },
        error: error => {
          this.carregando = false;
        }
      })
  }

  getChildComments(id: number) {
    var childComments = this.postComments.filter(c => c.repliedTo?.id == id);
    return childComments.length > 0 ? childComments : null;
  }

  emitCommentMadeEvent() {
    this.commentMadeEvent.emit();
  }

  likeComment() {
    this.lastLikeValue = this.lastLikeValue == true ? false : true;

    this.commentService.likeComment(this.lastLikeValue, this.comment.id).subscribe(
      {
        next: result => {
          this.likeClass = ['rated']
          this.dislikeClass = []
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
          this.likeClass = []
          this.dislikeClass = ['rated']
        },
        error: error => {
        }
      })
  }
}
