import { Component, ElementRef, EventEmitter, HostListener, Input, Output, Renderer2, ViewChild } from "@angular/core";
import { Block } from "../../core/shared/posts/post";

@Component({
  selector: 'text-div',
  templateUrl: './text-div.component.html',
  styleUrls: ['./text-div.component.scss']
})
export class TextDivComponent {
  @Output() onResizeEvent = new EventEmitter();
  @Output() resizeFinishedEvent = new EventEmitter();
  @Input() block: Block;

  @ViewChild('textContainer') textContainer: ElementRef;

  width: number;
  height: number;
  mouseMoveListener: Function;

  constructor(private renderer: Renderer2) { }

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

  @HostListener('window:mouseup', ['$event.target'])
  teste(el: any) {
    if (this.width && this.height)
      if (this.width != this.textContainer.nativeElement.offsetWidth
        || this.height != this.textContainer.nativeElement.offsetHeight) {
        this.width = this.textContainer.nativeElement.offsetWidth;
        this.height = this.textContainer.nativeElement.offsetHeight;
        this.resizeFinishedEvent.emit();
      }
  }

  @HostListener('document:mouseup')
  onMouseUp(el: any) {
    this.ngOnDestroy();
  }

  ngOnDestroy() {
    if (this.mouseMoveListener) {
      this.mouseMoveListener();
    }
  }
}