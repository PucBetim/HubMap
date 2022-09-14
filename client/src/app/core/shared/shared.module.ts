import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormErrorComponent } from './form-error/form-error.component';
import { LoadingComponent } from './loading/loading.component';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatIconModule } from '@angular/material/icon';
import { ToneHexColorPipe } from './pipe/tone-hex-color.pipe';

@NgModule({
  declarations: [
    FormErrorComponent, LoadingComponent, ToneHexColorPipe
  ],
  imports: [
    CommonModule,
    MatProgressSpinnerModule
  ],
  exports: [
    FormErrorComponent,
    LoadingComponent,
    MatIconModule,
    ToneHexColorPipe
  ]
})
export class SharedModule { }
