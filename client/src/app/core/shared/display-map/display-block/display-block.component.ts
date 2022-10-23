import { Component, Input, OnInit } from '@angular/core';
import { Block } from '../../posts/post';

@Component({
  selector: 'display-block',
  templateUrl: './display-block.component.html',
  styleUrls: ['./display-block.component.scss']
})
export class DisplayBlockComponent implements OnInit {

  @Input() block: Block;
  @Input() parentBlock: Block;
  @Input() resizeRatio: number;

  constructor() { }

  ngOnInit(): void {
  }
}
