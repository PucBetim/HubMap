import { Block, Position } from '../../core/shared/posts/map';
import { Component, Input, OnInit } from '@angular/core';

@Component({
  selector: 'line-svg',
  templateUrl: './line-svg.component.html',
  styleUrls: ['./line-svg.component.scss']
})
export class LineSvgComponent implements OnInit {

  @Input() block: Block;
  @Input() parentBlock: Block;

  blockCenter = new Position;
  parentBlockCenter = new Position;

  constructor() { }

  ngOnInit(): void {
    this.blockCenter.x = this.block.position.x + (this.block.size.width / 2);
    this.blockCenter.y = this.block.position.y + (this.block.size.height / 2);

    this.parentBlockCenter.x = this.parentBlock.position.x + (this.parentBlock.size.width / 2);
    this.parentBlockCenter.y = this.parentBlock.position.y + (this.parentBlock.size.height / 2);
  }

}
