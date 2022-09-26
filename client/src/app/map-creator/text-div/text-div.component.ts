import { Component, EventEmitter, HostListener, Input, Output, Renderer2, ViewChild } from "@angular/core";
import { Block } from "../../core/shared/posts/map";

@Component({
  selector: 'app-text-div',
  templateUrl: './text-div.component.html',
  styleUrls: ['./text-div.component.scss']
})
export class TextDivComponent {
  @Output() onResizeEvent = new EventEmitter();
  @Output() resizeFinishedEvent = new EventEmitter();
  @Input() block: Block;

  width: number;
  height: number;
  mouseMoveListener: Function;

  @HostListener('mousedown', ['$event.target'])
  onMouseDown(el: any) {
    this.width = el.offsetWidth;
    this.height = el.offsetHeight;
    this.mouseMoveListener = this.renderer.listen('document', 'mousemove', () => {
      if (this.width !== el.offsetWidth || this.height !== el.offsetHeight) {
        this.onResizeEvent.emit({ width: el.offsetWidth, height: el.offsetHeight });
      }
    });
  }

  @HostListener('mouseup', ['$event.target'])
  resizeFinished(el: any) {
    if (this.width != el.offsetWidth
      || this.height != el.offsetHeight) {
      this.resizeFinishedEvent.emit({ width: el.offsetWidth, height: el.offsetHeight });
    }
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