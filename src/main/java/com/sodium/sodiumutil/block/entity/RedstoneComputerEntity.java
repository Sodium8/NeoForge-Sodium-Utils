package com.sodium.sodiumutil.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class RedstoneComputerEntity extends BlockEntity {
    public RedstoneComputerEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.REDSTONE_COMPUTER_BLOCK_ENTITY.get(), pos, blockState);
    }

    private String compilingCode = "";

    public void tick() {
        System.out.println(compilingCode);
    }
    //  Integerjopa;jopa=rs.get("minecraft:stone","minecraft:air");if(jopa>4){rs.set("minecraft:air","minecraft:stone",6);}
    public String getCode(){
        return compilingCode;
    }
    public void setCode(String value) {
        this.compilingCode = value;
        setChanged();
    }
    class SodiumScriptProgram{
        class SodiumScriptVariable<T>{
            String name;
            T value;
            public SodiumScriptVariable(String name, T value){
                this.value = value;
                this.name = name;
            }
        }
        List<SodiumScriptVariable<Integer>> intVariables = new ArrayList<>();
        List<SodiumScriptVariable<Boolean>> boolVariables = new ArrayList<>();
        List<SodiumScriptVariable<String>> stringVariables = new ArrayList<>();
        public Object getVariable(String name){
            for (var i: intVariables){
                if (Objects.equals(i.name, name)){
                    return i.value;
                }
            }
            for (var i: boolVariables){
                if (Objects.equals(i.name, name)){
                    return i.value;
                }
            }
            for (var i: stringVariables){
                if (Objects.equals(i.name, name)){
                    return i.value;
                }
            }
            return null;
        }
        public void initVariable(String type, String name){
            if (Objects.equals(type, "Boolean")){
                this.boolVariables.add(new SodiumScriptVariable<>(name, false));
            }else if (Objects.equals(type, "String")){
                this.stringVariables.add(new SodiumScriptVariable<>(name, ""));
            } else if (Objects.equals(type, "Integer")){
                this.intVariables.add(new SodiumScriptVariable<>(name, 0));
            }
        }
        public void setVariable(String name, Object value){
            if (value instanceof String a){
                for (var i = 0; i<this.stringVariables.toArray().length; i++){
                    if (Objects.equals(this.stringVariables.get(i).name, name)){
                        this.stringVariables.get(i).value = a;
                    }
                }
            }
            if (value instanceof Integer a){
                for (var i = 0; i<this.intVariables.toArray().length; i++){
                    if (Objects.equals(this.intVariables.get(i).name, name)){
                        this.intVariables.get(i).value = a;
                    }
                }
            }
            if (value instanceof Boolean a){
                for (var i = 0; i<this.boolVariables.toArray().length; i++){
                    if (Objects.equals(this.boolVariables.get(i).name, name)){
                        this.boolVariables.get(i).value = a;
                    }
                }
            }
        }
        public void compile(String code){
            String removed_enters = code.replace("\n", "");
            removed_enters = removed_enters.replace(" ", "");
            List<String> commands = Arrays.stream(removed_enters.split(";")).toList();
            for (var i: commands){
                if (i.substring(0, 7).equals("Boolean")){
                    this.initVariable("Boolean", i.substring(7));
                }
                if (i.substring(0, 6).equals("String")){
                    this.initVariable("String", i.substring(6));
                }
                if (i.substring(0, 7).equals("Integer")){
                    this.initVariable("Integer", i.substring(7));
                }
                if (i.contains("=")){
                    Integer eqPos = i.indexOf('=');
                    this.setVariable(i.substring(0, eqPos), this.compileValue(i.substring(eqPos+1)));
                }
            }
        }
        public Object compileValue(String code){
            String removed_enters = code.replace("\n", "");
            removed_enters = removed_enters.replace(" ", "");
            if (removed_enters.contains("\"")){
                int first = removed_enters.indexOf("\"");
                String s = removed_enters.substring(0, first) + removed_enters.substring(first+1);
                int second = s.indexOf("\"");
                return removed_enters.substring(first+1, second);
            }
            else if (removed_enters.contains("+")){
                Integer eqPos = removed_enters.indexOf('+');
                return (Integer)this.compileValue(removed_enters.substring(0, eqPos)) + (Integer) this.compileValue(removed_enters.substring(eqPos+1));
            }else if (removed_enters.contains("-")){
                Integer eqPos = removed_enters.indexOf('-');
                return (Integer)this.compileValue(removed_enters.substring(0, eqPos)) - (Integer) this.compileValue(removed_enters.substring(eqPos+1));
            }else if (this.getVariable(code) != null){
                return this.getVariable(code);
            }
            return null;
        }
    }

}
