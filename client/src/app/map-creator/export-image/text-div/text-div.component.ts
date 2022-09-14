import { Component, Input, OnInit } from '@angular/core';
import { block } from '../../models/map';

@Component({
  selector: 'text-div',
  templateUrl: './text-div.component.html',
  styleUrls: ['./text-div.component.scss']
})
export class TextDivComponent implements OnInit {
  @Input() block: block;

  width: number;
  height: number;
  mouseMoveListener: Function;

  constructor() { }

  ngOnInit(): void {
  }
}
