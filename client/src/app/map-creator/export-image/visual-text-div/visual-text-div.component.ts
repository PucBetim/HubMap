import { Component, Input, OnInit } from '@angular/core';
import { block } from '../../models/map';

@Component({
  selector: 'visual-text-div',
  templateUrl: './visual-text-div.component.html',
  styleUrls: ['./visual-text-div.component.scss']
})
export class VisualTextDivComponent implements OnInit {
  @Input() block: block;

  constructor() { }

  ngOnInit(): void {
  }
}
