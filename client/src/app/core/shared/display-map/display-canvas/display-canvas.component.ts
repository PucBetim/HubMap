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
  @Input() size: number = 300;
  @Input() zoomSize: number;
  @Input() showOptions: boolean = true;

  visualPost: Post;
  resizeRatio: number;
  closestPoint = new Position;
  farestPoint = new Position;
  optionsStyle: string[];
  mapSize: number;
  public zoomed: boolean = false;

  constructor(
    private getLimitPoints: GetLimitPoints,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.mapSize = this.size;
    if (this.post.map[0]) {
      this.loadCanvas();
    }
  }

  loadCanvas() {
    this.visualPost = JSON.parse(JSON.stringify(this.post));

    if (this.visualPost.map) {
      var limit = this.getLimitPoints.getClosestFartest(this.visualPost.map)

      this.closestPoint = limit.closestPoint;
      this.farestPoint = limit.farestPoint;
      var PostOriginalWidth = this.farestPoint.x - this.closestPoint.x;
      var PostOriginalHeight = this.farestPoint.y - this.closestPoint.y;

      var widthRatio = (this.mapSize - 10) / PostOriginalWidth;
      var heightRatio = (this.mapSize - 10) / PostOriginalHeight;

      this.resizeRatio = widthRatio < heightRatio ? widthRatio : heightRatio;

      this.resizeBlocks(this.visualPost.map);
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
    this.router.navigateByUrl(`/details/${this.post.id}/${this.post.author.id}`);
  }

  zoom(zoom: boolean) {
    if (zoom) {
      this.zoomed = true;
      this.mapSize = this.zoomSize
    }
    else {
      this.zoomed = false;
      (this.mapSize = this.size)
    }

    this.loadCanvas();
  }
}



