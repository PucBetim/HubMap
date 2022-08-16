import { Component, ElementRef, EventEmitter, HostListener, Input, OnInit, Output } from '@angular/core';
import { block } from '../models/map';
import { line } from '../models/line';

@Component({
  selector: 'block',
  templateUrl: './block.component.html',
  styleUrls: ['./block.component.scss']
})
export class BlockComponent implements OnInit {

  public blockSelected: boolean = false;
  public line: line = new line;

  @Input() block: block;
  @Input() parentBlock: block;
  @Output() selectBlockEvent = new EventEmitter<block>();

  @HostListener('document:click', ['$event'])
  clickout(event: any) {
    if (!this.eRef.nativeElement.contains(event.target)) {
      if (this.blockSelected) this.blockSelected = false;
    }
  }

  constructor(private eRef: ElementRef) { }

  ngOnInit(): void {
  }

  clickInside() {
    this.blockSelected = true;
    this.emitSelect(this.block);
  }

  emitSelect(block: block) {
    this.selectBlockEvent.emit(block);
  }

  changePosition(event: any) {
    this.block.position.x = event.layerX - event.offsetX;
    this.block.position.y = event.layerY - event.offsetY;
  }

  resize(event: any) {
    this.block.size.width = event.width;
    this.block.size.height = event.height;
  }

  addBlock(location: string) {
    let dislocation = 50;

    let newHeight = this.block.size.height;
    let newWidth = this.block.size.width;
    let newBackgroundColor = this.block.backgroundColor;
    let newFontColor = this.block.fontColor;
    let newPositionX = this.block.position.x;
    let newPositionY = this.block.position.y;
    switch (location) {
      case 'above':
        newPositionY -= newHeight + dislocation;
        break;
      case 'right':
        newPositionX += newWidth + dislocation;
        break;
      case 'below':
        newPositionY += newHeight + dislocation;
        break;
      case 'left':
        newPositionX -= newWidth + dislocation;
        break;
    }

    let newBlock: block = new block;
    newBlock.content = "Digite seu texto aqui!";
    newBlock.size.height = newHeight;
    newBlock.size.width = newWidth;
    newBlock.backgroundColor = newBackgroundColor;
    newBlock.fontColor = newFontColor
    newBlock.position.x = newPositionX;
    newBlock.position.y = newPositionY;

    this.block.blocks.push(newBlock);
  }
}
