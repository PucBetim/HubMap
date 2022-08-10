import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { DragDropModule } from '@angular/cdk/drag-drop';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatTooltipDefaultOptions, MatTooltipModule, MAT_TOOLTIP_DEFAULT_OPTIONS } from '@angular/material/tooltip';
import { MatMenuModule } from '@angular/material/menu';
import { FormsModule } from '@angular/forms';
import {MatButtonToggleModule} from '@angular/material/button-toggle';

import { CreationComponent } from './creation/creation.component';
import { CreatorRoutes } from './map-creator.routing';
import { ClickOutsideModule } from 'ng-click-outside';
import { ColorPickerModule } from 'ngx-color-picker';
import { StyleEditorComponent } from './style-editor/style-editor.component';

export const OtherOptions: MatTooltipDefaultOptions = {
  showDelay: 0,
  hideDelay: 0,
  touchGestures: 'auto',
  position: 'below',
  touchendHideDelay: 0,
  disableTooltipInteractivity: true,
}

@NgModule({
  declarations: [
    CreationComponent,
    StyleEditorComponent
  ],
  imports: [
    CommonModule,
    RouterModule.forChild(CreatorRoutes),
    DragDropModule,
    MatIconModule,
    MatButtonModule,
    MatTooltipModule,
    FormsModule,
    MatMenuModule,
    MatButtonToggleModule,
    ClickOutsideModule,
    ColorPickerModule
  ],
  providers: [
    {provide: MAT_TOOLTIP_DEFAULT_OPTIONS, useValue: OtherOptions}
  ]
})
export class MapCreatorModule { }
