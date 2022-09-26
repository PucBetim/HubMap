import { Component, HostListener, OnInit } from '@angular/core';

@Component({
  selector: 'app-landing',
  templateUrl: './landing.component.html',
  styleUrls: ['./landing.component.scss']
})
export class LandingComponent implements OnInit {


  @HostListener('window:resize', ['$event'])
  onResize() {
    if (window.innerWidth > 1400 && this.blocksSize != 500)
      this.resizeMapDisplay(500);
    else if (window.innerWidth > 1200 && window.innerWidth < 1401 && this.blocksSize != 300)
      this.resizeMapDisplay(300);
    else if (window.innerWidth > 800 && window.innerWidth < 1201 && this.blocksSize != 200)
      this.resizeMapDisplay(200);
    else if (window.innerWidth < 801 && this.blocksSize != 100)
      this.resizeMapDisplay(100);
  }


  public searchBarClass: string[] = ["searchBar"];
  public resultDivClass: string[] = ["resultDiv"];
  public results: boolean = false;
  public map = new Map;
  public blocksSize: number = 300;

  constructor() { }

  ngOnInit(): void {
  }

  resizeMapDisplay(newSize: number) {
    this.blocksSize = newSize
    this.results = false;
    setTimeout(() => {
      this.results = true;
    }, 1000)
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
