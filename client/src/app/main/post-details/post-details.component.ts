import { Subscription } from 'rxjs';
import { PostService } from './../../core/shared/posts/post-blocks.service';
import { Component, HostListener, OnInit } from '@angular/core';
import { Post } from 'src/app/core/shared/posts/post';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'post-map-details',
  templateUrl: './post-details.component.html',
  styleUrls: ['./post-details.component.scss']
})
export class PostDetailsComponent implements OnInit {

  @HostListener('window:resize', ['$event'])
  onResize() {
    if (window.innerWidth > 1400 && this.mapSize != 500)
      this.resizeMapDisplay();
    else if (window.innerWidth > 1200 && window.innerWidth < 1401 && this.mapSize != 300)
      this.resizeMapDisplay();
    else if (window.innerWidth > 800 && window.innerWidth < 1201 && this.mapSize != 200)
      this.resizeMapDisplay();
    else if (window.innerWidth < 801 && this.mapSize != 100)
      this.resizeMapDisplay();
  }
  
  public carregando: boolean = false;
  public result: boolean = false;
  public post: Post;
  public sub: Subscription;
  public mapSize: number = window.innerHeight / 1.8;

  constructor(
    private postService: PostService,
    private route: ActivatedRoute,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.sub = this.route.params.subscribe(
      params => {
        this.getPost(params['id']);
      });
  }

  resizeMapDisplay() {
    this.mapSize = window.innerHeight / 1.5;
    this.result = false;
    setTimeout(() => {
      this.result = true;
    }, 200)
  }

  getPost(id: number) {
    this.carregando = true;
    this.postService.getPostById(id).subscribe(
      {
        next: result => {
          this.post = result.body;
          this.carregando = false;
          this.result = true;
        },
        error: error => {
          console.log(error);
          this.carregando = false;
        }
      })
  }

  goToEdit() {
    this.router.navigateByUrl(`/creator/${this.post.id}`);
  }
}
