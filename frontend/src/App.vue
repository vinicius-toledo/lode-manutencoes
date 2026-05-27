<script setup>

import { ssrExportNameKey } from 'vite/module-runner'
import { ref, onMounted, computed } from 'vue'


const equipamentos = ref([])
const busca = ref('')

const carregarEquipamentos = async () =>{
  try{ 
    const resposta = await fetch('http://localhost:8080/api/equipamentos')
    if(resposta.ok){
      equipamentos.value = await resposta.json()
    } else {
      console.error('Erro ao carregar equipamentos da API')
    }
  }catch(error){
    console.error('Não foi possivel conectar à API', error)
  }
}

const equipamentosFiltrados = computed(() =>{
  return equipamentos.value.filter(equipamento => 
    equipamento.nome.toLowerCase().includes(busca.value.toLowerCase())
  )
})

const obterCorStatus = (status) => {
  if (!status) return 'status-padrao'
  const s = status.toLowerCase()
  if (s.includes('critico') || s.includes('crítico')) return 'status-critico'
  if (s.includes('manutencao') || s.includes('manutenção')) return 'status-manutencao'
  return 'status-operacional' 
}

onMounted(() => {
  carregarEquipamentos()
})
</script>

<template>
  <div class="container">
    <header>
      <h1>Sistema de Monitoramento de Equipamentos </h1>
      <p>Gestão e controle de manutenção em tempo real</p>
    </header>

    <div class="busca-container">
      <input v-model="busca" 
             type="text" 
             placeholder="Digite o nome do equipamento para filtrar..." 
             class="input-busca"
             />
    </div>

    <main class="grid-equipamentos">
      <div 
          v-for="equipamento in equipamentosFiltrados" 
          :key="equipamento.id" 
          class="card"
      >
        <div class="card-header">
          <h3>{{ equipamento.nome }}</h3>
          <span :class="['badge' , obterCorStatus(equipamento.status)]">
            {{ equipamento.status }}
          </span>
        </div>

        <div class="card-body">
          <p><strong>Tipo:</strong>{{ equipamento.tipo || 'Não informado'}}</p>
          <p><strong>Instalação:</strong> {{ equipamento.dataInstalacao || '---' }}</p>
        </div>
    </div>

    <div v-if="equipamentosFiltrados.length === 0" class="sem-dados">
      <p>Nenhum equipamento encontrado com o nome "{{ busca }}"</p>
    </div>
    </main>
  </div>
</template>
