import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-landing',
  templateUrl: './landing.component.html',
  styleUrls: ['./landing.component.scss']
})
export class LandingComponent implements OnInit {

  public searchBarClass: string[] = ["searchBar"];
  public resultDivClass: string[] = ["resultDiv"];
  public results: boolean = false;

  public map = new Map;
  
  constructor() { }

  ngOnInit(): void {
  }

  searchMaps() {
    this.results = true;
    this.searchBarClass.push("sbTop");
    this.resultDivClass.push("rdTop");

    var _map = JSON.parse(localStorage.getItem('mapa') || '{}');

    if (_map.blocks)
      this.map = _map;
  }
}
