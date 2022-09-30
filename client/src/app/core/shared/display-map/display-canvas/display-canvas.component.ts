import { PostService } from './../../posts/post.service';
import { Component, Input, OnInit } from '@angular/core';
import { GetLimitPoints } from 'src/app/map-creator/getLimitPoints';
import { Block, Position, Post } from '../../posts/post';

@Component({
  selector: 'display-canvas',
  templateUrl: './display-canvas.component.html',
  styleUrls: ['./display-canvas.component.scss']
})
export class DisplayCanvasComponent implements OnInit {

  @Input() post: Post;
  @Input() size: number;

  visualPost: Post;
  resizeRatio: number;
  closestPoint = new Position;
  farestPoint = new Position;
  optionsStyle: string[];

  constructor(
    private getLimitPoints: GetLimitPoints,
    private postService: PostService
  ) { }

  ngOnInit(): void {
    this.visualPost = JSON.parse(JSON.stringify(this.post));
    this.postService.getPostBlocks(this.post.id).subscribe({
      next: result => {
        this.visualPost.blocks = result.body;
        this.loadCanvas();
      }, error: erro => {
        console.log(erro)
      }
    })
  }

  loadCanvas() {
    if (this.visualPost.blocks) {

      var limit = this.getLimitPoints.getClosestFartest(this.visualPost.blocks)

      this.closestPoint = limit.closestPoint;
      this.farestPoint = limit.farestPoint;
      var PostOriginalWidth = this.farestPoint.x - this.closestPoint.x;
      var PostOriginalHeight = this.farestPoint.y - this.closestPoint.y;

      var widthRatio = (this.size - 10) / PostOriginalWidth;
      var heightRatio = (this.size - 10) / PostOriginalHeight;

      this.resizeRatio = widthRatio < heightRatio ? widthRatio : heightRatio;

      this.resizeBlocks(this.visualPost.blocks);
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
}



