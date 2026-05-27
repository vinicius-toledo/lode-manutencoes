<script setup>

import { ssrExportNameKey } from 'vite/module-runner'
import { ref, onMounted, computed } from 'vue'


const equipamentos = ref([])
const busca = ref('')

const carregarEquipamentos = async () =>{
  try{ 
    const resposta = await fetch('http://localhost:8080/api/equipamentos')
    if(resposta.ok){
      const dados = await resposta.json()
      equipamentos.value = dados.content 
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

<style scoped>
/* Estilos estruturais e de fontes */
.container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 2rem;
  font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
  color: #333;
}

header {
  text-align: center;
  margin-bottom: 2.5rem;
}

header h1 {
  color: #1a202c;
  font-size: 2.2rem;
  margin-bottom: 0.5rem;
}

header p {
  color: #718096;
}

/* Customização do Input de Busca */
.busca-container {
  margin-bottom: 2rem;
}

.input-busca {
  width: 100%;
  padding: 12px 20px;
  font-size: 1rem;
  border: 2px solid #e2e8f0;
  border-radius: 8px;
  outline: none;
  transition: border-color 0.2s;
}

.input-busca:focus {
  border-color: #42b883; /* Cor verde oficial do Vue */
}

/* Layout da Grid Responsiva */
.grid-equipamentos {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 1.5rem;
}

/* Estilização Moderna dos Cards */
.card {
  background: white;
  border-radius: 12px;
  box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.1), 0 2px 4px -1px rgba(0, 0, 0, 0.06);
  border: 1px solid #edf2f7;
  overflow: hidden;
  display: flex;
  flex-direction: column;
}

.card-header {
  padding: 1.25rem;
  border-bottom: 1px solid #edf2f7;
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  gap: 10px;
}

.card-header h3 {
  font-size: 1.15rem;
  margin: 0;
  color: #2d3748;
}

.card-body {
  padding: 1.25rem;
}

.card-body p {
  margin: 0.5rem 0;
  font-size: 0.95rem;
  color: #4a5568;
}

/* Estilização das Badges com Cores do Teste */
.badge {
  padding: 4px 10px;
  border-radius: 9999px;
  font-size: 0.75rem;
  font-weight: bold;
  text-transform: uppercase;
  white-space: nowrap;
}

.status-operacional {
  background-color: #c6f6d5;
  color: #22543d;
}

.status-manutencao {
  background-color: #feebc8;
  color: #744210;
}

.status-critico {
  background-color: #fed7d7;
  color: #742a2a;
}

.status-padrao {
  background-color: #e2e8f0;
  color: #4a5568;
}

.sem-dados {
  grid-column: 1 / -1;
  text-align: center;
  padding: 3rem;
  color: #a0aec0;
  font-size: 1.1rem;
}
</style>
