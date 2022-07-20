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
    let bloc2 = new block;
    bloc2.content = "Texto 2 texto maior pra teste";
    this.map.blocks = [bloc, bloc2];
  }

}
