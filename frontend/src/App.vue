<script setup>

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


