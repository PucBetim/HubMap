import { Component, ElementRef, EventEmitter, HostListener, Input, OnInit, Output } from '@angular/core';
import { Block, Position } from '../../posts/post';

@Component({
  selector: 'visual-block',
  templateUrl: './visual-block.component.html',
  styleUrls: ['./visual-block.component.scss']
})
export class VisualBlockComponent implements OnInit {

  public afterImagePosition: Position = { x: 0, y: 0 };
  public afterImageSize: number;

  @Input() block: Block;
  @Input() parentBlock: Block;

  constructor(private eRef: ElementRef) { }

  ngOnInit(): void {
  }

}
