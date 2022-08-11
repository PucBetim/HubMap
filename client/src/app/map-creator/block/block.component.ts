import { Component, ElementRef, HostListener, Input, OnInit } from '@angular/core';
import { block } from '../models/map';

@Component({
  selector: 'block',
  templateUrl: './block.component.html',
  styleUrls: ['./block.component.scss']
})
export class BlockComponent implements OnInit {

  public clickInside: boolean = true;
  public displayEditStyle: boolean = false;

  constructor(private eRef: ElementRef) { }

  @Input() block: block;

  @HostListener('document:click', ['$event'])
  clickout(event: any) {
    if (this.eRef.nativeElement.contains(event.target)) {
      this.clickInside = true;
    }
    else
    this.clickInside = false;
  }

  ngOnInit(): void {
  }

  editStyle() {
    this.displayEditStyle = true;
  }

  changePosition(event: any, block: block) {
    block.position.x = event.layerX - event.offsetX;
    block.position.y = event.layerY - event.offsetY;
  }

  resize(event: any, block: block) {
    block.size.width = event.target.clientWidth;
    block.size.height = event.target.clientHeight;
  }

}
