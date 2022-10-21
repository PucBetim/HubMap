import { Subscription } from 'rxjs';
import { Component, OnInit } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Post } from 'src/app/core/shared/posts/post';
import { PostService } from 'src/app/core/shared/posts/post-blocks.service';
import { ActivatedRoute, Router } from '@angular/router';

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

  constructor(private postService: PostService, private snackBar: MatSnackBar) { }

  ngOnInit(): void {
  }

  searchMaps() {
    this.loading = true;
    this.results = false;

    this.postService.getPublicPosts().subscribe(
      {
        next: result => {
          this.postsResult = result.body;
          if (this.postsResult.length > 0) {
            this.results = true;
          }
          this.loading = false;
        },
        error: error => {
          this.snackBar.open("Falha ao obter mapas! Tente novamente mais tarde.", "Ok");
          this.loading = false;
        }
      })
  }
}
