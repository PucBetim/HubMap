import { Router } from '@angular/router';
import { Post } from 'src/app/core/shared/posts/post';
import { Component, HostListener, OnInit } from '@angular/core';
import { PostService } from 'src/app/core/shared/posts/post-blocks.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

@Component({
  selector: 'app-landing',
  templateUrl: './landing.component.html',
  styleUrls: ['./landing.component.scss']
})
export class LandingComponent implements OnInit {


  @HostListener('window:resize', ['$event'])
  onResize() {
    if (window.innerWidth > 1400 && this.blocksSize != 300)
      this.resizeMapDisplay(300);
    else if (window.innerWidth > 800 && window.innerWidth < 1401 && this.blocksSize != 250)
      this.resizeMapDisplay(250);
    else if (window.innerWidth < 801 && this.blocksSize != 200)
      this.resizeMapDisplay(200);
  }

  public loading: boolean = false;
  public searchBarClass: string[] = ["searchBar"];
  public resultDivClass: string[] = ["resultDiv"];
  public results: boolean = false;
  public postsResult: Post[];
  public blocksSize: number;
  public form: FormGroup;

  constructor(private postService: PostService, private snackBar: MatSnackBar, private router: Router, private fb: FormBuilder,

  ) { }

  ngOnInit(): void {
    this.form = this.fb.group({
      search: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(80)]],
    });

    this.onResize();
  }

  resizeMapDisplay(newSize: number) {
    this.blocksSize = newSize
    this.loading = true;
    setTimeout(() => {
      this.loading = false;
    }, 1000)
  }

  // search() {
  //   this.loading = true;
  //   this.results = false;

  //   this.postService.getPublicPosts().subscribe(
  //     {
  //       next: result => {
  //         this.postsResult = result.body;
  //         if (this.postsResult.length > 0) {
  //           this.searchBarClass.push("sbTop");
  //           this.resultDivClass.push("rdTop");
  //           this.results = true;
  //         }
  //         this.loading = false;
  //       },
  //       error: error => {
  //         this.snackBar.open("Falha ao obter mapas! Tente novamente mais tarde.", "Ok");
  //         this.loading = false;
  //       }
  //     })
  // }

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
            this.snackBar.open("Nenhum mapa encontrado!", "Ok", {
              duration: 2000
            });
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
      this.postService.getPostById(p.trim()).subscribe(
        {
          next: result => {
            this.postsResult.push(result.body);
            this.results = true;
          },
          error: error => {
            this.snackBar.open("Erro ao obter mapa! Tente novamente mais tarde.", "Ok", {
              duration: 2000
            });
          }
        })
    });
    this.searchBarClass.push("sbTop");
    this.resultDivClass.push("rdTop");
    this.loading = false;
  }
}
