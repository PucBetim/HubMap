import { Component, OnInit, ViewChild } from '@angular/core';
import { MatMenuTrigger } from '@angular/material/menu';
import { line } from '../models/line';

import { map, block } from './../models/map';

@Component({
  selector: 'app-creation',
  templateUrl: './creation.component.html',
  styleUrls: ['./creation.component.scss']
})
export class CreationComponent implements OnInit {

  @ViewChild('editTrigger') editTrigger: MatMenuTrigger;

  public map = new map;
  public selectedBlock: block;
  public lines: line[] = [];

  constructor() { }

  ngOnInit(): void {
    this.map = JSON.parse(localStorage.getItem('mapa') || '{}');
  }

  changePosition(event: any, block: block) {
    block.position.x = event.layerX - event.offsetX - 5;
    block.position.y = event.layerY - event.offsetY - 5;
  }

  addNewBlock() {
    let _block = new block;
    _block.content = "Editar";
    _block.position.x = 100;
    _block.position.y = 100;
    _block.size.width = 150;
    _block.size.height = 75;

    if (this.map.blocks == null) {
      let _map = new map;
      _map.blocks[0] = _block;
      this.map = _map
      return
    }
    this.map.blocks.push(_block);
  }

  selectBlock(block: block) {
    this.selectedBlock = block;
  }

  deleteBlock(blocks: block[]) {
    console.log(blocks)
    for (let i = 0; i < blocks.length; i++) {
      if (blocks[i] == this.selectedBlock) {
        const index = blocks.indexOf(this.selectedBlock, 0);
        if (index > -1) {
          blocks.splice(index, 1);
        }
      }
      else
        this.deleteBlock(blocks[i].blocks)
    }
  }

  save() {
    localStorage.setItem('mapa', JSON.stringify(this.map));
  }


  resizeEvent(event: any){
    console.log('x')
  }
}
