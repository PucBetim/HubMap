import { map, block, position } from './../models/map';
import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-creation',
  templateUrl: './creation.component.html',
  styleUrls: ['./creation.component.scss']
})
export class CreationComponent implements OnInit {

  public map = new map;
  constructor() { }

  ngOnInit(): void {
    this.map = JSON.parse(localStorage.getItem('mapa') || '{}');
    // var x = document.getElementsByClassName("block");
    // console.log(x)

  }

  changePosition(event: any, block: block) {
    block.position.x = event.layerX - event.offsetX;
    block.position.y = event.layerY - event.offsetY;
  }

  resize(event: any, block: block) {
    block.size.width = event.target.clientWidth - 26;
    block.size.height = event.target.clientHeight - 4;
  }

  addNewBlock() {
    let _block = new block;
    _block.content = "Aqui!";
    _block.position.x = 100;
    _block.position.y = 100;
    _block.size.width = 150;
    _block.size.height = 75;
    console.log(_block)

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

  onResize(position: any) {
    console.log(document.getElementsByClassName('canvas'))
  }
}

