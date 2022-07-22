import { map, block, position } from './../models/map';
import { Component, OnInit } from '@angular/core';
import { FileSaverService } from 'ngx-filesaver';
import { FileSaverOptions } from 'file-saver';
import { CdkDragDrop } from '@angular/cdk/drag-drop';

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
  }

  changePosition(event: any, block: block) {
    console.log(event)
    block.position.x = event.layerX - event.offsetX;
    block.position.y = event.layerY - event.offsetY;
    console.log(block.position)
  }

  addNewBlock() {
    let _block = new block;
    _block.content = "Aqui!";
    _block.position.x = 100;
    _block.position.y = 100;

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

