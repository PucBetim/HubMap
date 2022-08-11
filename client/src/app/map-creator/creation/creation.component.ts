import { Component, OnInit, ViewChild } from '@angular/core';
import { MatMenuTrigger } from '@angular/material/menu';

import { map, block } from './../models/map';

@Component({
  selector: 'app-creation',
  templateUrl: './creation.component.html',
  styleUrls: ['./creation.component.scss']
})
export class CreationComponent implements OnInit {

  @ViewChild('editTrigger') editTrigger: MatMenuTrigger;

  public map = new map;
  public selected: block[] = [];
  public editMode: boolean = true;
  public disableDrag: boolean = false;

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

  save() {
    localStorage.setItem('mapa', JSON.stringify(this.map));
  }
}
