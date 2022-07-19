import { map, block } from './../models/map';
import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-creation',
  templateUrl: './creation.component.html',
  styleUrls: ['./creation.component.scss']
})
export class CreationComponent implements OnInit {

  public map = new map;

  constructor() {
  }

  ngOnInit(): void {
    let bloc = new block;
    bloc.content = "Texto";
    this.map.blocks = [bloc];


  }

  x() {
    var bc = document.getElementsByClassName("block");
    console.log(bc)
  }

}
