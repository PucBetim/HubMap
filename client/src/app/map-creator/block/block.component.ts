import { Component, ElementRef, EventEmitter, HostListener, Input, OnInit, Output } from '@angular/core';
import { block, position } from '../models/map';

@Component({
  selector: 'block',
  templateUrl: './block.component.html',
  styleUrls: ['./block.component.scss']
})
export class BlockComponent implements OnInit {

  public clickInside: boolean = true;
  public displayEditStyle: boolean = false;

  @Input() block: block;
  @Output() selectBlockEvent = new EventEmitter<block>();;

  @HostListener('document:click', ['$event'])
  clickout(event: any) {
    if (this.eRef.nativeElement.contains(event.target))
      this.clickInside = true;
    else
      this.clickInside = false;
  }

  constructor(private eRef: ElementRef) { }

  ngOnInit(): void {
  }

  editStyle() {
    this.displayEditStyle = true;
    this.emitSelect(this.block);
  }

  emitSelect(block: block) {
    this.displayEditStyle
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
    let newBlock = new block;

    newBlock.content = "Digite seu texto aqui!"
    newBlock.size = this.block.size
    newBlock.backgroundColor = this.block.backgroundColor;
    newBlock.fontColor = this.block.fontColor;


    let newPosition = this.block.position;
    switch (location) {
      case 'above':
        newPosition.y -= dislocation;
        break;
      case 'right':
        newPosition.x += dislocation;
        break;
      case 'below':
        newPosition.y += dislocation;
        break;
      case 'left':
        newPosition.x -= dislocation;
        break;
    }

    newBlock.position = newPosition;
    this.block.blocks.push(newBlock);
  }

}
