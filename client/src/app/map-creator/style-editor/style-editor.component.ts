import { Component, ElementRef, EventEmitter, HostListener, Input, OnInit, Output, ViewChild, ViewEncapsulation } from '@angular/core';

import { block } from './../models/map';
import { fontSizes } from 'src/app/core/shared/font-sizes';
import { colors } from 'src/app/core/shared/colors';
import { MatMenuTrigger } from '@angular/material/menu';

@Component({
  selector: 'style-editor',
  templateUrl: './style-editor.component.html',
  styleUrls: ['./style-editor.component.scss']
})
export class StyleEditorComponent implements OnInit {

  public loaded: boolean = false;
  public fontSizes = fontSizes;
  public colors = colors;

  constructor(private eRef: ElementRef) { }

  @Input() block: block;
  @Input() clickedInside: boolean;
  @Output() closeEvent = new EventEmitter<string>();

  @ViewChild('triggerBckg', { static: false }) triggerBkg: MatMenuTrigger;
  @ViewChild('triggerFont', { static: false }) triggerFont: MatMenuTrigger;
  @ViewChild('triggerSize', { static: false }) triggerSize: MatMenuTrigger;

  @HostListener('document:click', ['$event'])
  clickout(event: any) {
    if (!this.eRef.nativeElement.contains(event.target) && this.loaded && !this.clickedInside)
      this.closeEvent.emit()

    this.loaded = true
  }

  ngOnInit(): void {
  }

  stopPropagation(event: any) {
    if (event) event.stopPropagation();
  }

  style(style: string, event: any) {

    if (event) event.stopPropagation();

    switch (style) {
      case "bold":
        this.block.fontWeight = this.block.fontWeight === "normal" ? "bold" : "normal"
        break;
      case "italic":
        this.block.fontStyle = this.block.fontStyle === "normal" ? "italic" : "normal"
        break;
      case "underline":
        this.block.textDecoration = this.block.textDecoration == "none" ? "underline" : "none"
        break;
      case "alignLeft":
        this.block.textAlign = "left";
        break;
      case "alignCenter":
        this.block.textAlign = "center";
        break;
      case "alignRight":
        this.block.textAlign = "right";
        break;
      case "justify":
        this.block.textAlign = "justify";
        break;
      default:
        break;
    }
  }

  changeColor(color: string) {
    this.block.backgroundColor = color;
  }

  closeMenu(menu: string) {
    switch (menu) {
      case "fontSize":
        this.triggerSize.closeMenu();
        break;
      case "fontColor":
        this.triggerFont.closeMenu();
        break;
      case "bckgColor":
        this.triggerBkg.closeMenu();
        break;
      default:
        break;
    }
  }
}
