import { ConfigService } from './../../core/services/config.service';
import { Component, Inject, Input, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { Post } from 'src/app/core/shared/posts/post';
import { PostService } from 'src/app/core/shared/posts/post-blocks.service';

@Component({
  selector: 'app-create-post',
  templateUrl: './create-post.component.html',
  styleUrls: ['./create-post.component.scss']
})
export class CreatePostComponent implements OnInit {

  public form: FormGroup;
  public loading: boolean = false;
  public post: Post;
  public errors: any[] = [];
  public editorMode: boolean = false;
  public userLogged: boolean = true;

  public descMaxLength: number = 200;

  constructor(
    public dialogRef: MatDialogRef<CreatePostComponent>,
    private postService: PostService,
    private fb: FormBuilder,
    @Inject(MAT_DIALOG_DATA) public data: any
  ) { }

  ngOnInit(): void {
    if (!ConfigService.getUser())
      this.userLogged = false;

    this.editorMode = this.data.editorMode;
    this.post = this.data.post;

    this.form = this.fb.group({
      title: ['', [Validators.required, Validators.maxLength(30)]],
      description: ['', [Validators.minLength(3), Validators.maxLength(this.descMaxLength)]],
      private: [false]
    });

    if (this.editorMode) this.patchFormValues();
  }

  patchFormValues() {
    this.form.patchValue({
      title: this.post.title,
      description: this.post.description,
      private: this.post.private,
    })
  }

  createOrUpdatePost() {
    if (this.userLogged) {
      this.loading = true;

      let p = Object.assign({}, this.form.value);
      if (!this.editorMode) {
        this.postService.postPost(p).subscribe({
          next: obj => {
            this.postBlocks(obj.body.dataId)
            this.loading = false;
          }, error: error => {

            this.errors = error.errors;
            this.loading = false;
          }
        })
      }
      else {
        let p = Object.assign({}, this.form.value);
        this.postService.updatePost(p, this.post.id).subscribe({
          next: obj => {
            this.updateBlocks(obj.body.dataId)
            this.loading = false;
          }, error: error => {
            this.errors = error.errors;
            this.loading = false;
          }
        })
      }
    }
  }

  updateBlocks(id: string) {
    this.post.map.forEach(b => {
      this.loading = true;
      this.postService.updateBlocks(b, id).subscribe({
        next: obj => {
          this.loading = false;
          this.dialogRef.close({ id: id, msg: "Mapa editado com sucesso!" });
        }, error: error => {
          this.errors = error.errors;
          this.loading = false;
        }
      })
    });
  }

  postBlocks(id: string) {
    this.post.map.forEach(b => {
      this.loading = true;
      this.postService.postBlocks(b, id).subscribe({
        next: obj => {
          this.loading = false;
          this.dialogRef.close({ id: id, msg: "Mapa postado com sucesso!" });
        }, error: error => {
          this.errors = error.errors;
          this.loading = false;
        }
      })
    });
  }

  close(link: string) {
    this.dialogRef.close({ link: link })
  }
}
