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

  width: number = 550;
  height: number = 550;

  closestPoint = new Position;
  farestPoint = new Position;

  constructor(private getLimitPoints: GetLimitPoints) { }

  ngOnInit(): void {
    console.log(this.map)

    var limit = this.getLimitPoints.getClosestFartest(this.map.blocks)
    this.closestPoint = limit.closestPoint;
    this.farestPoint = limit.farestPoint;

    var mapOriginalWidth = this.farestPoint.x - (this.farestPoint.x - this.closestPoint.x);
    var mapOriginalHeight = this.farestPoint.y - (this.farestPoint.y - this.closestPoint.y);
  }

}
