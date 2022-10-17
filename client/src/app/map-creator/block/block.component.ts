import { Position, Block, Post } from '../../core/shared/posts/post';
import { Component, ElementRef, EventEmitter, HostListener, Input, OnInit, Output, ViewChild } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'block',
  templateUrl: './block.component.html',
  styleUrls: ['./block.component.scss']
})
export class BlockComponent implements OnInit {

  public blockSelected: boolean = false;
  public afterImagePosition: Position = { x: 0, y: 0 };
  public afterImageSize: number = 50;
  public initialContent: string;

  @Input() block: Block;
  @Input() parentBlock: Block;
  @Input() rootPost: Post;

  @Output() selectBlockEvent = new EventEmitter<Block>();
  @Output() unselectBlockEvent = new EventEmitter<any>();
  @Output() saveProgressEvent = new EventEmitter<any>();

  @HostListener('document:click', ['$event'])
  clickout(event: any) {
    if (!this.eRef.nativeElement.contains(event.target)) {

      if (this.blockSelected)
        this.onDrag(event, true)

      this.blockSelected = false;
      this.emitUnselect();

      if (this.initialContent != this.block.content)
        this.emitSaveProgress();
    }
  }

  constructor(private eRef: ElementRef, private snackBar: MatSnackBar) { }

  ngOnInit(): void {
    this.initialContent = this.block.content;
    this.afterImagePosition = { x: (this.block.position.x + (this.block.size.width / 2) - this.afterImageSize / 2), y: (this.block.position.y + (this.block.size.height / 2) - this.afterImageSize / 2) };
  }

  clickInside() {
    this.blockSelected = true;
    this.emitSelect(this.block);
  }

  emitSelect(block: Block) {
    this.selectBlockEvent.emit(block);
  }

  emitUnselect() {
    this.unselectBlockEvent.emit();
  }

  emitSaveProgress() {
    this.saveProgressEvent.emit();
  }

  onDrag(event: any, overlap: boolean) {
    if (overlap) {
      var lastPosition = new Position;
      lastPosition.x = this.afterImagePosition.x - (this.block.size.width / 2) + this.afterImageSize / 2;
      lastPosition.y = this.afterImagePosition.y - (this.block.size.height / 2) + this.afterImageSize / 2;
      this.block.position = lastPosition;
    }
    else {
      this.block.position.x = event.layerX - event.offsetX;
      this.block.position.y = event.layerY - event.offsetY;
      this.afterImagePosition = { x: (this.block.position.x + (this.block.size.width / 2) - this.afterImageSize / 2), y: (this.block.position.y + (this.block.size.height / 2) - this.afterImageSize / 2) };
      this.emitSaveProgress();
    }
  }

  onResize(event: any) {
    this.block.size.width = event.width;
    this.block.size.height = event.height;

    this.afterImagePosition = { x: (this.block.position.x + (this.block.size.width / 2) - this.afterImageSize / 2), y: (this.block.position.y + (this.block.size.height / 2) - this.afterImageSize / 2) };
  }


  onStyleAndSave(block: Block) {
    this.block = block;
    this.emitSaveProgress();
  }


  addBlock(location: string) {
    let dislocation = 50;
    let newBlock = JSON.parse(JSON.stringify(this.block))
    newBlock.id = null;
    newBlock.blocks = [];
    newBlock.content = "Clique para editar";

    var closestGap: Position = { x: this.block.position.x, y: this.block.position.y }
    var farestGap: Position = { x: 3840 - (this.block.position.x + this.block.size.width + 10), y: 2160 - (this.block.position.y + this.block.size.height + 10) }

    switch (location) {
      case 'above':
        if (closestGap.y > (newBlock.size.height + dislocation))
          newBlock.position.y -= newBlock.size.height + dislocation; else {
          this.notifySpace()
          return
        };
        break;
      case 'right':
        if (farestGap.x > (newBlock.size.width + dislocation))
          newBlock.position.x += newBlock.size.width + dislocation; else {
          this.notifySpace()
          return
        };

        break;
      case 'below':
        if (farestGap.y > (newBlock.size.height + dislocation))
          newBlock.position.y += newBlock.size.height + dislocation; else {
          this.notifySpace()
          return
        };

        break;
      case 'left':
        if (closestGap.x > (newBlock.size.width + dislocation))
          newBlock.position.x -= newBlock.size.width + dislocation; else {
          this.notifySpace()
          return
        };
        break;
    }
    this.block.blocks.push(newBlock);
    this.emitSaveProgress();
  }

  notifySpace() {
    this.snackBar.open("Pouco espaço para colocar novo bloco!", "Ok", {
      duration: 2000
    })
  }
}