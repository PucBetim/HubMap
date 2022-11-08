import { Subscription } from 'rxjs';
import { Component, OnInit } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Post } from 'src/app/core/shared/posts/post';
import { PostService } from 'src/app/core/shared/posts/post-blocks.service';
import { ActivatedRoute, Router } from '@angular/router';
import { Validators, FormBuilder, FormGroup } from '@angular/forms';

@Component({
  selector: 'app-search-maps',
  templateUrl: './search-maps.component.html',
  styleUrls: ['./search-maps.component.scss']
})
export class SearchMapsComponent implements OnInit {

  public loading: boolean = false;
  public postsResult: Post[];
  public results: boolean = false;
  public sub: Subscription;
  public form: FormGroup;

  constructor(private postService: PostService, private snackBar: MatSnackBar, private fb: FormBuilder) { }

  ngOnInit(): void {
    this.form = this.fb.group({
      search: ['', [Validators.required, Validators.minLength(2), Validators.maxLength(80)]],
    });
  }

  search() {
    this.postsResult = [];
    this.loading = true;
    this.results = false;
    let p = Object.assign({}, this.form.value);

    this.postService.searchPosts(p).subscribe(
      {
        next: result => {
          if (result.body?.dataId)
            this.getPosts(result.body.dataId.split(','))
          else {
            this.loading = false;
            this.results = true;
            this.snackBar.open("Nenhum mapa encontrado!", "Ok");
          }
        },
        error: error => {
          this.snackBar.open("Falha ao obter mapas! Tente novamente mais tarde.", "Ok", {
            duration: 2000
          });
          this.loading = false;
        }
      })
  }

  getPosts(postsIds: string[]) {
    postsIds.forEach(p => {
      this.postService.getPublicPostsById(p.trim()).subscribe(
        {
          next: result => {
            this.postsResult.unshift(result.body);
            this.results = true;
          },
          error: error => {
            this.snackBar.open("Erro ao obter mapa! Tente novamente mais tarde.", "Ok", {
              duration: 2000
            });
          }
        })
    });
    this.loading = false;
  }
}
