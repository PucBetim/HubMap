import { Block } from './../../../core/shared/posts/map';
import { GetLimitPoints } from './../../../map-creator/getLimitPoints';
import { Component, Input, OnInit } from '@angular/core';
import { Map, Position } from 'src/app/core/shared/posts/map';

@Component({
  selector: 'display-canvas',
  templateUrl: './display-canvas.component.html',
  styleUrls: ['./display-canvas.component.scss']
})
export class DisplayCanvasComponent implements OnInit {

  @Input() map: Map;
  @Input() size: number;

  visualMap: Map;
  resizeRatio: number;
  closestPoint = new Position;
  farestPoint = new Position;

  constructor(private getLimitPoints: GetLimitPoints) { }

  ngOnInit(): void {

    this.visualMap = JSON.parse(JSON.stringify(this.map))

    var limit = this.getLimitPoints.getClosestFartest(this.visualMap.blocks)

    this.closestPoint = limit.closestPoint;
    this.farestPoint = limit.farestPoint;

    var mapOriginalWidth = this.farestPoint.x - this.closestPoint.x;
    var mapOriginalHeight = this.farestPoint.y - this.closestPoint.y;

    var widthRatio = (this.size - 10) / mapOriginalWidth;
    var heightRatio = (this.size - 10) / mapOriginalHeight;

    this.resizeRatio = widthRatio < heightRatio ? widthRatio : heightRatio;

    this.resizeBlocks(this.visualMap.blocks);
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
