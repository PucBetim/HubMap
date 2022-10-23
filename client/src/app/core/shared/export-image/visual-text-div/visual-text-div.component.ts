import { Component, Input, OnInit } from '@angular/core';
import { Block } from '../../posts/post';

@Component({
  selector: 'visual-text-div',
  templateUrl: './visual-text-div.component.html',
  styleUrls: ['./visual-text-div.component.scss']
})
export class VisualTextDivComponent implements OnInit {
  @Input() block: Block;

  constructor() { }

  ngOnInit(): void {
  }
}
