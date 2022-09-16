import { Component, ElementRef, EventEmitter, HostListener, Input, OnInit, Output } from '@angular/core';
import { block, position } from '../../models/map';

@Component({
  selector: 'visual-block',
  templateUrl: './visual-block.component.html',
  styleUrls: ['./visual-block.component.scss']
})
export class VisualBlockComponent implements OnInit {

  public afterImagePosition: position = { x: 0, y: 0 };
  public afterImageSize: number;

  @Input() block: block;
  @Input() parentBlock: block;

  constructor(private eRef: ElementRef) { }

  ngOnInit(): void {
  }

}
