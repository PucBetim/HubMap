import { Block } from './../../../core/shared/posts/map';
import { Component, Input, OnInit } from '@angular/core';

@Component({
  selector: 'display-block',
  templateUrl: './display-block.component.html',
  styleUrls: ['./display-block.component.scss']
})
export class DisplayBlockComponent implements OnInit {

  @Input() block: Block;
  @Input() parentBlock: Block;

  constructor() { }

  ngOnInit(): void {
  }

}
