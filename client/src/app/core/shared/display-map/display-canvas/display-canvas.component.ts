import { PostService } from '../../posts/post-blocks.service';
import { Component, Input, OnInit } from '@angular/core';
import { GetLimitPoints } from 'src/app/map-creator/getLimitPoints';
import { Block, Position, Post } from '../../posts/post';
import { Router } from '@angular/router';

@Component({
  selector: 'display-canvas',
  templateUrl: './display-canvas.component.html',
  styleUrls: ['./display-canvas.component.scss']
})
export class DisplayCanvasComponent implements OnInit {

  @Input() post: Post;
  @Input() size: number;
  @Input() showOptions: boolean = true;
  
  visualPost: Post;
  resizeRatio: number;
  closestPoint = new Position;
  farestPoint = new Position;
  optionsStyle: string[];
  public carregando: boolean = false;

  constructor(
    private getLimitPoints: GetLimitPoints,
    private postService: PostService,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.carregando = true;
    this.visualPost = JSON.parse(JSON.stringify(this.post));
    this.postService.getPostBlocks(this.post.id).subscribe({
      next: result => {
        this.visualPost.map = [result.body];        
        this.carregando = false;
        this.loadCanvas();
      }, error: erro => {
        console.log(erro)
        this.carregando = false;
      }
    })
  }

  async loadCanvas() {
    if (this.visualPost.map) {
      var limit = this.getLimitPoints.getClosestFartest(this.visualPost.map)

      this.closestPoint = limit.closestPoint;
      this.farestPoint = limit.farestPoint;
      var PostOriginalWidth = this.farestPoint.x - this.closestPoint.x;
      var PostOriginalHeight = this.farestPoint.y - this.closestPoint.y;

      var widthRatio = (this.size - 10) / PostOriginalWidth;
      var heightRatio = (this.size - 10) / PostOriginalHeight;

      this.resizeRatio = widthRatio < heightRatio ? widthRatio : heightRatio;

      await this.resizeBlocks(this.visualPost.map);
    }
  }

  resizeBlocks(blocks: Block[]) {
    blocks.forEach(b => {

      b.size.width = b.size.width * this.resizeRatio;
      b.size.height = b.size.height * this.resizeRatio;

      b.position.x = (b.position.x - this.closestPoint.x) * this.resizeRatio;
      b.position.y = (b.position.y - this.closestPoint.y) * this.resizeRatio;

      b.fontSize *= this.resizeRatio;

      this.resizeBlocks(b.blocks);
      return;
    });
  }

  goToDetails() {
    this.router.navigateByUrl(`/details/${this.post.id}`);
  }
}



