import { CanvasService } from 'src/app/core/services/canvas.service';
import { PostService } from '../../posts/post-blocks.service';
import { Component, Input, OnInit } from '@angular/core';
import { GetLimitPoints } from 'src/app/map-creator/getLimitPoints';
import { Block, Position, Post } from '../../posts/post';
import { Router } from '@angular/router';
import { ConfigService } from 'src/app/core/services/config.service';

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
  public result: boolean = false;
  public optionsClass: string[] = [];
  public showTitle: boolean = true;
  public margin = CanvasService.displayCanvasMargin;;

  constructor(
    private getLimitPoints: GetLimitPoints,
    private router: Router,
  ) { }

  ngOnInit(): void {
    this.mapSize = this.size;
    if (this.showOptions) this.optionsClass = ['optionMode']
    this.loadCanvas();
  }

  onMouseEnter() {
    this.optionsStyle = ['hover']
    this.showTitle = false;
  }
  onMouseLeave() {
    this.optionsStyle = []
    this.showTitle = true;
  }

  loadCanvas() {
    this.visualPost = JSON.parse(JSON.stringify(this.post));

    if (this.visualPost.map) {
      this.result = true;
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
    if (this.showOptions) {
      this.router.navigateByUrl(`/details/${this.post.id}/${this.post.author.id}`);
    }
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



