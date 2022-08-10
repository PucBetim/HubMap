import { block } from './../models/map';
import { Component, ElementRef, EventEmitter, HostListener, Input, OnInit, Output } from '@angular/core';

@Component({
  selector: 'style-editor',
  templateUrl: './style-editor.component.html',
  styleUrls: ['./style-editor.component.scss']
})
export class StyleEditorComponent implements OnInit {

  constructor(private eRef: ElementRef) { }

  @Input() block: block;
  @Output() closeEvent = new EventEmitter<string>();
  public loaded: boolean = false;

  ngOnInit(): void {
    console.log(this.block)
  }

  @HostListener('document:click', ['$event'])
  clickout(event: any) {
    if (!this.eRef.nativeElement.contains(event.target) && this.loaded) {
      this.closeEvent.emit()
    }
    this.loaded = true
  }


  stopPropagation(event: any) {
    if (event) event.stopPropagation();
  }

  style(style: string, block: block, event: any) {

    if (event) event.stopPropagation();

    switch (style) {
      case "bold":
        block.fontWeight = block.fontWeight === "normal" ? "bold" : "normal"
        break;
      case "italic":
        block.fontStyle = block.fontStyle === "normal" ? "italic" : "normal"
        break;
      case "underline":
        block.textDecoration = block.textDecoration == "none" ? "underline" : "none"
        break;
      default:
        break;
    }
  }
}
