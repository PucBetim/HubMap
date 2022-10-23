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
      searchTerm: ['', [Validators.required]],
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

  searchMaps() {
    this.loading = true;
    this.results = false;

    this.postService.getPublicPosts().subscribe(
      {
        next: result => {
          this.postsResult = result.body;
          if (this.postsResult.length > 0) {
            this.searchBarClass.push("sbTop");
            this.resultDivClass.push("rdTop");
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
