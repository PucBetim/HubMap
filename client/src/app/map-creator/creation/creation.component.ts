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
  public displayEditStyle: boolean = false;
  public blockEdit: block;

  constructor() { }

  ngOnInit(): void {
    this.map = JSON.parse(localStorage.getItem('mapa') || '{}');
  }

  changePosition(event: any, block: block) {
    block.position.x = event.layerX - event.offsetX - 5;
    block.position.y = event.layerY - event.offsetY - 5;
  }

  resize(event: any, block: block) {
    block.size.width = event.target.clientWidth;
    block.size.height = event.target.clientHeight;
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

  stopPropagation(event: any) {
    if (event) event.stopPropagation();
  }

  // style(style: string, block: block, event: any) {

  //   if (event) event.stopPropagation();

  //   switch (style) {
  //     case "bold":
  //       block.fontWeight = block.fontWeight === "normal" ? "bold" : "normal"
  //       break;
  //     case "italic":
  //       block.fontStyle = block.fontStyle === "normal" ? "italic" : "normal"
  //       break;
  //     case "underline":
  //       block.textDecoration = block.textDecoration == "none" ? "underline" : "none"
  //       break;
  //     default:
  //       break;
  //   }
  // }


  editStyle(block: block) {
    this.displayEditStyle = true;
    this.blockEdit = block;
  }
}
