import { Component, EventEmitter, HostListener, Input, Output, Renderer2 } from "@angular/core";
import { block } from "../models/map";


@Component({
  selector: 'app-textarea',
  templateUrl: './textarea.component.html',
  styleUrls: ['./textarea.component.scss']
})
export class TextareaComponent {
  @Output() resize = new EventEmitter();
  @Input() block: block;
  
  width: number;
  height: number;

  mouseMoveListener: Function;

  @HostListener('mousedown', ['$event.target'])
  onMouseDown(el: any) {
    this.width = el.offsetWidth;
    this.height = el.offsetHeight;
    this.mouseMoveListener = this.renderer.listen('document', 'mousemove', () => {
      if (this.width !== el.offsetWidth || this.height !== el.offsetHeight) {
        this.resize.emit({ width: el.offsetWidth, height: el.offsetHeight });
      }
    });
  }

  @HostListener('document:mouseup')
  onMouseUp(el: any) {
    this.ngOnDestroy();
  }

  constructor(private renderer: Renderer2) { }

  ngOnDestroy() {
    if (this.mouseMoveListener) {
      this.mouseMoveListener();
    }
  }
}