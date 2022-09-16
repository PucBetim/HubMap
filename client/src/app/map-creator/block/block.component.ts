import { position, block, size } from './../models/map';
import { Component, ElementRef, EventEmitter, HostListener, Input, OnInit, Output } from '@angular/core';

@Component({
  selector: 'block',
  templateUrl: './block.component.html',
  styleUrls: ['./block.component.scss']
})
export class BlockComponent implements OnInit {

  public blockSelected: boolean = false;
  public afterImagePosition: position = { x: 0, y: 0 };
  public afterImageSize: number;

  @Input() block: block;
  @Input() parentBlock: block;
  @Output() selectBlockEvent = new EventEmitter<block>();
  @Output() unselectBlockEvent = new EventEmitter<any>();
  @Output() saveProgressEvent = new EventEmitter<any>();

  @HostListener('document:click', ['$event'])
  clickout(event: any) {
    if (!this.eRef.nativeElement.contains(event.target)) {
      this.blockSelected = false;
      this.emitUnselect();
    }
  }

  constructor(private eRef: ElementRef) { }

  ngOnInit(): void {
    this.afterImageSize = (this.block.size.width + this.block.size.height) / 10
    this.afterImagePosition = { x: (this.block.position.x + (this.block.size.width / 2) - this.afterImageSize / 2), y: (this.block.position.y + (this.block.size.height / 2) - this.afterImageSize / 2) };
  }

  clickInside() {
    this.blockSelected = true;
    this.emitSelect(this.block);
  }

  emitSelect(block: block) {
    this.selectBlockEvent.emit(block);
  }

  emitUnselect() {
    this.unselectBlockEvent.emit();
  }

  emitSaveProgress() {
    this.saveProgressEvent.emit();
  }

  onDrag(event: any) {
    this.block.position.x = event.layerX - event.offsetX;
    this.block.position.y = event.layerY - event.offsetY;

    this.afterImagePosition = { x: (this.block.position.x + (this.block.size.width / 2) - this.afterImageSize / 2), y: (this.block.position.y + (this.block.size.height / 2) - this.afterImageSize / 2) };
    this.emitSaveProgress();
  }

  onResize(event: any) {
    this.block.size.width = event.width;
    this.block.size.height = event.height;

    this.afterImagePosition = { x: (this.block.position.x + (this.block.size.width / 2) - this.afterImageSize / 2), y: (this.block.position.y + (this.block.size.height / 2) - this.afterImageSize / 2) };
    this.afterImageSize = (this.block.size.width + this.block.size.height) / 10
  }

  resizedFinished(event: any) {
    this.emitSaveProgress();
  }

  onStyleAndSave(block: block) {
    this.block = block;
    this.emitSaveProgress();
  }

  addBlock(location: string) {
    let dislocation = 50;
    let newBlock = JSON.parse(JSON.stringify(this.block))
    newBlock.blocks = [];
    newBlock.content = "Clique duplo para editar";

    switch (location) {
      case 'above':
        newBlock.position.y -= newBlock.size.height + dislocation;
        break;
      case 'right':
        newBlock.position.x += newBlock.size.width + dislocation;
        break;
      case 'below':
        newBlock.position.y += newBlock.size.height + dislocation;
        break;
      case 'left':
        newBlock.position.x -= newBlock.size.width + dislocation;
        break;
    }

    this.block.blocks.push(newBlock);
    this.emitSaveProgress();
  }
}
