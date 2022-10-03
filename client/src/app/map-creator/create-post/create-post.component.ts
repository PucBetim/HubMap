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
  public carregando: boolean = false;
  public post: Post;
  public errors: any[] = [];
  public editorMode: boolean = false;

  constructor(
    public dialogRef: MatDialogRef<CreatePostComponent>,
    private postService: PostService,
    private fb: FormBuilder,
    @Inject(MAT_DIALOG_DATA) public data: any
  ) { }

  ngOnInit(): void {
    this.editorMode = this.data.editorMode;
    this.post = this.data.post;
    this.form = this.fb.group({
      title: ['', [Validators.required, Validators.maxLength(30)]],
      description: ['', [Validators.maxLength(500)]],
      private: [false, [Validators.required]]
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
    this.carregando = true;
    let p = Object.assign({}, this.form.value);
    if (!this.editorMode) {
      this.postService.post(p).subscribe({
        next: obj => {
          this.postBlocks(obj.body.dataId)
          this.carregando = false;
        }, error: error => {
          this.errors = error.errors;
          this.carregando = false;
        }
      })
    }
    else {
      let p = Object.assign({}, this.form.value);

      this.postService.updatePost(p, this.post.id).subscribe({
        next: obj => {
          this.updateBlocks(obj.body.dataId)
          this.carregando = false;
        }, error: error => {
          this.errors = error.errors;
          this.carregando = false;
        }
      })
    }
  }

  updateBlocks(id: number) {
    this.post.map.forEach(b => {
      this.carregando = true;
      this.postService.updateBlocks(b, id).subscribe({
        next: obj => {
          this.carregando = false;
          this.dialogRef.close("Mapa editado com sucesso!");
        }, error: error => {
          this.errors = error.errors;
          this.carregando = false;
        }
      })
    });
  }

  postBlocks(id: number) {
    this.post.map.forEach(b => {
      this.carregando = true;
      this.postService.postBlocks(b, id).subscribe({
        next: obj => {
          this.carregando = false;
          this.dialogRef.close("Mapa postado com sucesso!");
        }, error: error => {
          this.errors = error.errors;
          this.carregando = false;
        }
      })
    });
  }
}
