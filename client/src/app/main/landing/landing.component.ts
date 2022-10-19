import { Post } from 'src/app/core/shared/posts/post';
import { Component, HostListener, OnInit } from '@angular/core';
import { PostService } from 'src/app/core/shared/posts/post-blocks.service';

@Component({
  selector: 'app-landing',
  templateUrl: './landing.component.html',
  styleUrls: ['./landing.component.scss']
})
export class LandingComponent implements OnInit {


  @HostListener('window:resize', ['$event'])
  onResize() {
    if (window.innerWidth > 1400 && this.blocksSize != 500)
      this.resizeMapDisplay(500);
    else if (window.innerWidth > 1200 && window.innerWidth < 1401 && this.blocksSize != 300)
      this.resizeMapDisplay(300);
    else if (window.innerWidth > 800 && window.innerWidth < 1201 && this.blocksSize != 200)
      this.resizeMapDisplay(200);
  }

  public loading: boolean = false;
  public searchBarClass: string[] = ["searchBar"];
  public resultDivClass: string[] = ["resultDiv"];
  public results: boolean = false;
  public postsResult: Post[];
  public blocksSize: number = 300;

  constructor(private postService: PostService) { }

  ngOnInit(): void {
  }

  resizeMapDisplay(newSize: number) {
    this.blocksSize = newSize
    this.results = false;
    setTimeout(() => {
      this.results = true;
    }, 1000)
  }

  searchMaps() {
    this.loading = true;
    this.results = false;
    this.searchBarClass.push("sbTop");
    this.resultDivClass.push("rdTop");

    this.postService.getPublicPosts().subscribe(
      {
        next: result => {
          this.postsResult = result.body;
          this.loading = false;
          this.results = true;

        },
        error: error => {
          console.log(error)
          this.loading = false;
          this.results = true;
        }
      })
  }
}
