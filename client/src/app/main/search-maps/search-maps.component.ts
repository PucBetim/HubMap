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
  public otherResults: Post[];
  public results: boolean = false;
  public currentIndex: number = 0;
  public sub: Subscription;
  public form: FormGroup;
  public resultIndexes: string[];
  public publicPage: number = 0;

  constructor(
    private postService: PostService,
    private snackBar: MatSnackBar,
    private fb: FormBuilder,
    private route: ActivatedRoute,
    private router: Router) { }

  ngOnInit(): void {
    this.form = this.fb.group({
      search: ['', [Validators.required, Validators.minLength(2), Validators.maxLength(255)]],
    });

    this.sub = this.route.params.subscribe(
      params => {
        var search = params['q'];
        if (search) {
          this.form.patchValue({ search: search })
          this.search();
        }
      });
  }

  submit() {
    let p = Object.assign({}, this.form.value);
    this.router.navigate(['/search/', { q: p.search }]);
  }

  search() {
    this.publicPage = 0;
    this.postsResult = [];
    this.otherResults = [];
    this.resultIndexes = [];
    this.loading = true;
    this.results = false;
    let p = Object.assign({}, this.form.value);
    this.postService.searchPosts(p).subscribe(
      {
        next: result => {
          if (result.body?.dataId) {
            this.resultIndexes = result.body.dataId.split(',');
            this.getPosts();
          }
          else {
            this.getPublicPosts();
          }
        },
        error: error => {
          this.snackBar.open("Falha ao obter mapas! Tente novamente mais tarde.", "Ok", {
            duration: 5000
          });
          this.loading = false;
        }
      })
  }

  loadMore() {
    this.currentIndex += 10;
    this.getPosts();
  }

  async getPosts() {
    var srcArr = this.resultIndexes.slice(this.currentIndex, this.currentIndex + 10)
    var resultArray: Post[] = [];

    for await (const post of srcArr.map(async p => {
      const post = await this.postService.getPublicPostsById(p.trim()).toPromise();
      resultArray.push(post.body)
    }));

    this.postsResult = this.postsResult.concat(this.orderArray(resultArray, this.resultIndexes))
    this.loading = false;
    this.results = true;
  }

  orderArray(array: Post[], order: string[]) {
    var newArray: Post[] = [];
    order.forEach(e => {
      array.forEach(a => {
        if (a.id.toString() == e) newArray.push(a)
      })
    });
    return newArray;
  };

  loadMorePublic() {
    this.publicPage++;
    this.getPublicPosts();
  }

  getPublicPosts() {
    this.loading = true;
    this.postService.getPublicPosts(10, this.publicPage, true, "views, likes").subscribe({
      next: result => {
        if (result.body)
        this.otherResults = this.otherResults.concat(result.body);
        this.loading = false;
        this.results = true;
      },
      error: error => {
        this.snackBar.open("Falha ao obter mapas! Tente novamente mais tarde.", "Ok", {
          duration: 5000
        });
        this.loading = false;
      }
    })
  };
}