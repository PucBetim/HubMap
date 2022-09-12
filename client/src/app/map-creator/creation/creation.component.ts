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
  public blockSelected: boolean = false;
  public savedProgress: [block[]] = [[]];

  constructor() { }

  ngOnInit(): void {
    this.map = JSON.parse(localStorage.getItem('mapa') || '{}');
    this.savedProgress = [JSON.parse(JSON.stringify(this.map.blocks))];
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
    this.saveProgress();
  }

  selectBlock(block: block) {
    this.selectedBlock = block;
    this.blockSelected = true;
  }

  unselectBlock() {
    this.blockSelected = false;
  }

  deleteBlock(blocks: block[]) {
    this.blockSelected = false;
    for (let i = 0; i < blocks.length; i++) {
      if (blocks[i] == this.selectedBlock) {
        const index = blocks.indexOf(this.selectedBlock, 0);
        if (index > -1) {
          blocks.splice(index, 1);
        }
        this.saveProgress();
      }
      else
        this.deleteBlock(blocks[i].blocks)
    }
  }

  undo() {
    if (this.savedProgress.length > 1) {
      this.map.blocks = JSON.parse(JSON.stringify(this.savedProgress[this.savedProgress.length - 2]));
      this.savedProgress.splice(-1)
    }
  }

  saveProgress() {
    if (this.savedProgress.length < 15) {
      const blocks = JSON.parse(JSON.stringify(this.map.blocks));
      this.savedProgress.push(blocks)
    }
    console.log(this.savedProgress)
  }

  save() {
    localStorage.setItem('mapa', JSON.stringify(this.map));
  }

}
