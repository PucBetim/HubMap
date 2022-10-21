import { Component, ElementRef, EventEmitter, HostListener, Input, OnInit, Output, ViewChild, ViewEncapsulation } from '@angular/core';

import { Block } from '../../core/shared/posts/post';
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

  @Input() block: Block;
  @Output() styleAndSaveEvent = new EventEmitter<Block>();

  @ViewChild('triggerRadius', { static: false }) triggerRadius: MatMenuTrigger;
  @ViewChild('triggerBckg', { static: false }) triggerBkg: MatMenuTrigger;
  @ViewChild('triggerFont', { static: false }) triggerFont: MatMenuTrigger;
  @ViewChild('triggerSize', { static: false }) triggerSize: MatMenuTrigger;

  ngOnInit(): void {
  }

  stopPropagation(event: any) {
    if (event) event.stopPropagation();
  }

  styleAndSave() {
    this.styleAndSaveEvent.emit(this.block)
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
        if (this.block.textAlign != "left")
          this.block.textAlign = "left";
        else return;
        break;
      case "alignCenter":
        if (this.block.textAlign != "center")
          this.block.textAlign = "center";
        else return;
        break;
      case "alignRight":
        if (this.block.textAlign != "right")
          this.block.textAlign = "right";
        else return;
        break;
      case "justify":
        if (this.block.textAlign != "justify")
          this.block.textAlign = "justify";
        else return;
        break;
      case "borderStyle":

        break;
      default:
        break;
    }
    this.styleAndSave();
  }

  changeColor(color: string) {
    this.block.backgroundColor = color;
    this.styleAndSave();
  }

  formatLabel(value: number) {
    return value + ' px';
  }

  closeMenu(menu: string) {
    switch (menu) {
      case "borderRadius":
        this.triggerRadius.closeMenu();
        break;
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
