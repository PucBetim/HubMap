import { size } from './../models/map';
import { Component, ElementRef, EventEmitter, HostListener, Input, OnInit, Output } from '@angular/core';
import { block, position } from '../models/map';

@Component({
  selector: 'block',
  templateUrl: './block.component.html',
  styleUrls: ['./block.component.scss']
})
export class BlockComponent implements OnInit {

  public blockSelected: boolean = false;

  @Input() block: block;
  @Output() selectBlockEvent = new EventEmitter<block>();;

  constructor() { }

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
    this.block.size.width = event.target.clientWidth;
    this.block.size.height = event.target.clientHeight;
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
