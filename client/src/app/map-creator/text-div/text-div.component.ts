import { AfterViewInit, Component, ElementRef, EventEmitter, HostListener, Input, Output, Renderer2, ViewChild } from "@angular/core";
import { Block } from "../../core/shared/posts/post";

@Component({
  selector: 'text-div',
  templateUrl: './text-div.component.html',
  styleUrls: ['./text-div.component.scss']
})
export class TextDivComponent implements AfterViewInit {
  @Output() onResizeEvent = new EventEmitter();
  @Output() resizeFinishedEvent = new EventEmitter();
  @Output() blockValidEvent = new EventEmitter<boolean>;
  @Input() block: Block;

  @ViewChild('textContainer') textContainer: ElementRef;
  @ViewChild('textDiv') textDiv: ElementRef;

  width: number;
  height: number;
  mouseMoveListener: Function;
  public valid: boolean = true;

  constructor(private renderer: Renderer2) { }

  ngAfterViewInit(): void {
    this.textDiv.nativeElement.addEventListener('paste', this.onPaste.bind(this))
  }

  onPaste(event: any) {
    event.preventDefault();
    var text = event.clipboardData.getData('text/plain');
    document.execCommand('insertText', false, text)

    //////////////// Não permitir colar mais de 300 
    // var newText = this.textDiv.nativeElement.innerHTML;
    // var length = newText.length;
    // if (length > 299)
    //   newText = newText.slice(0, -(length - 299));
    // this.block.content = newText;
  }

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