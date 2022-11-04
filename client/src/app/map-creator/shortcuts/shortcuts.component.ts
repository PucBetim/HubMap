import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'shortcuts',
  templateUrl: './shortcuts.component.html',
  styleUrls: ['./shortcuts.component.scss']
})
export class ShortcutsComponent implements OnInit {

  constructor() { }

  public shortcutList = [
    { key: 'Ctrl + Z', desc: 'Desfazer ação' },
    { key: 'Delete', desc: 'Deletar Bloco', condition: 'Bloco Selecionado' },
    { key: 'Ctrl + S', desc: 'Salvar Mapa' },
    { key: 'Ctrl + E', desc: 'Copiar Estilo do Bloco', condition: 'Bloco Selecionado' },
    { key: 'Ctrl + R', desc: 'Aplicar Estilo ao Bloco', condition: 'Bloco Selecionado' },
    { key: 'Ctrl + Seta', desc: 'Adiciona Bloco Filho', condition: 'Bloco Selecionado' },
  ]
  ngOnInit(): void {
  }

}
